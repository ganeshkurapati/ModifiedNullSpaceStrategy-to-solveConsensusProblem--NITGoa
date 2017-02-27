classdef K3Supervisor < simiam.controller.Supervisor
%% SUPERVISOR switches between controllers and handles their inputs/outputs.
%
% Properties:
%   current_controller      - Currently selected controller
%   controllers             - List of available controllers
%   goal_points             - Set of goal points
%   goal_index              - Pointer to current goal point
%   v                       - Robot velocity
%
% Methods:
%   execute - Selects and executes the current controller.

% Copyright (C) 2013, Georgia Tech Research Corporation
% see the LICENSE file included with this software

    properties
    %% PROPERTIES
    
        states
        eventsd
        
        current_state
    
    
        prev_ticks          % Previous tick count on the left and right wheels
        v
        goal
        is_blending
        d_stop
        d_at_obs
        d_unsafe
        p
    end
    
    methods
    %% METHODS
        
        function obj = K3Supervisor()
        %% SUPERVISOR Constructor
            obj = obj@simiam.controller.Supervisor();
            
            % initialize the controllers
            obj.controllers{1} = simiam.controller.AvoidObstacles();
            obj.controllers{2} = simiam.controller.GoToGoal();
            obj.controllers{3} = simiam.controller.GoToAngle();
            obj.controllers{4} = simiam.controller.AOandGTG();
            obj.controllers{5} = simiam.controller.Stop();
            
            % set the initial controller
            obj.current_controller = obj.controllers{5};
            obj.current_state = 5;
            
            % generate the set of states
            for i = 1:length(obj.controllers)
                obj.states{i} = struct('state', obj.controllers{i}.type, ...
                                       'controller', obj.controllers{i});
            end
            
            % define the set of eventsd
            obj.eventsd{1} = struct('event', 'at_obstacle', ...
                                   'callback', @at_obstacle);
            
            obj.eventsd{2} = struct('event', 'at_goal', ...
                                   'callback', @at_goal);
            
            obj.eventsd{3} = struct('event', 'obstacle_cleared', ...
                                    'callback', @obstacle_cleared);
                                
            obj.eventsd{4} = struct('event', 'unsafe', ...
                                    'callback', @unsafe);
                               
            obj.prev_ticks = struct('left', 0, 'right', 0);
            
            %% START CODE BLOCK %%
            obj.v               = 0.1;
            obj.goal            = [1;1];
            obj.is_blending     = false;
            obj.d_stop          = 0.05; %0.05 
            obj.d_at_obs        = 0.13;                
            obj.d_unsafe        = 0.1;
            %% END CODE BLOCK %%
            
            obj.p = simiam.util.Plotter();
            obj.current_controller.p = obj.p;
        end
        
        function execute(obj, dt)
        %% EXECUTE Selects and executes the current controller.
        %   execute(obj, robot) will select a controller from the list of
        %   available controllers and execute it.
        %
        %   See also controller/execute
        
            inputs = obj.controllers{4}.inputs; 
            inputs.v = obj.v;
            inputs.x_g = obj.goal(1);
            inputs.y_g = obj.goal(2);    
                    
            if(obj.is_blending)
                % The following (finite state machine) logic sets the
                % blending controller as the current controller until the
                % robot arrives at the goal.
                
                if(obj.check_event('at_goal'))
                    obj.switch_to_state('stop');
                else
                    obj.switch_to_state('ao_and_gtg');
                end
                
                outputs = obj.current_controller.execute(obj.robot, obj.state_estimate, inputs, dt);
                 fprintf('(v,w) = (%0.3f,%0.3f)\n', outputs.v, outputs.w);
                [vel_r, vel_l] = obj.robot.dynamics.uni_to_diff(outputs.v, outputs.w);
                obj.robot.set_wheel_speeds(vel_r, vel_l);
            else
                %% START CODE BLOCK %%
                   if obj.check_event('at_obstacle')
                       obj.switch_to_state('ao_and_gtg');
                   end
                   if obj.check_event('unsafe')
                       obj.switch_to_state('avoid_obstacles');
                   end
                   if obj.check_event('obstacle_cleared')
                       obj.switch_to_state('go_to_goal');
                   end
                   if obj.check_event('at_goal')
                       obj.switch_to_state('stop');
                   end
                %obj.switch_to_state('stop');
                
                %% END CODE BLOCK %%
                
                outputs = obj.current_controller.execute(obj.robot, obj.state_estimate, inputs, dt);
                
                [vel_r, vel_l] = obj.robot.dynamics.uni_to_diff(outputs.v, outputs.w);
                obj.robot.set_wheel_speeds(vel_r, vel_l);
            end
            
            obj.update_odometry();
%             [x, y, theta] = obj.state_estimate.unpack();
%             fprintf('current_pose: (%0.3f,%0.3f,%0.3f)\n', x, y, theta);
        end
        
        %% Events %%
        
        function rc = at_obstacle(obj, state, robot)
            ir_distances = obj.robot.get_ir_distances();
            rc = false;                                     % Assume initially that the robot is clear of obstacle
            
            % Loop through and test the sensors (only the front set)
            if any(ir_distances(2:7) < obj.d_at_obs)
                rc = true;
            end
        end
        
        function rc = unsafe(obj, state, robot)
            ir_distances = obj.robot.get_ir_distances();              
            rc = false;             % Assume initially that the robot is clear of obstacle
            
            % Loop through and test the sensors (only the front set)
            if any(ir_distances(2:7) < obj.d_unsafe)
                    rc = true;
            end
        end

        function rc = at_goal(obj, state, robot)
            [x,y,theta] = obj.state_estimate.unpack();
            rc = false;
            
            % Test distance from goal
            if norm([x - obj.goal(1); y - obj.goal(2)]) < obj.d_stop
                rc = true;
            end
        end

        function rc = obstacle_cleared(obj, state, robot)
            ir_distances = obj.robot.get_ir_distances();
            rc = false;              % Assume initially that the robot is clear of obstacle
            
            % Loop through and test the sensors (only front set)
            if all(ir_distances(2:7) > obj.d_at_obs)
                rc = true;
            end
        end
        
        %% State machine support functions
        
        function set_current_controller(obj, ctrl)
            % save plots
            obj.current_controller = ctrl;
            obj.p.switch_2d_ref();
            obj.current_controller.p = obj.p;
        end
        
        function rc = is_in_state(obj, name)
            rc = strcmp(name, obj.states{obj.current_state}.state);
        end
        
        function switch_to_state(obj, name)
            
            if(~obj.is_in_state(name))
                for i=1:numel(obj.states)
                    if(strcmp(obj.states{i}.state, name))
                        obj.set_current_controller(obj.states{i}.controller);
                        obj.current_state = i;
                        fprintf('switching to state %s\n', name);
                        return;
                    end
                end
            else
%                 fprintf('already in state %s\n', name);
                return
            end
            
            fprintf('no state exists with name %s\n', name);
        end
        
        function rc = check_event(obj, name)
           for i=1:numel(obj.eventsd)
               if(strcmp(obj.eventsd{i}.event, name))
                   rc = obj.eventsd{i}.callback(obj, obj.states{obj.current_state}, obj.robot);
                   return;
               end
           end
           
           % return code (rc)
           fprintf('no event exists with name %s\n', name);
           rc = false;
        end
        
        %% Odometry
        
        function update_odometry(obj)
        %% UPDATE_ODOMETRY Approximates the location of the robot.
        %   obj = obj.update_odometry(robot) should be called from the
        %   execute function every iteration. The location of the robot is
        %   updated based on the difference to the previous wheel encoder
        %   ticks. This is only an approximation.
        %
        %   state_estimate is updated with the new location and the
        %   measured wheel encoder tick counts are stored in prev_ticks.
        
            % Get wheel encoder ticks from the robot
            right_ticks = obj.robot.encoders(1).ticks;
            left_ticks = obj.robot.encoders(2).ticks;
            
            % Recal the previous wheel encoder ticks
            prev_right_ticks = obj.prev_ticks.right;
            prev_left_ticks = obj.prev_ticks.left;
            
            % Previous estimate 
            [x, y, theta] = obj.state_estimate.unpack();
            
            % Compute odometry here
            R = obj.robot.wheel_radius;
            L = obj.robot.wheel_base_length;
            m_per_tick = (2*pi*R)/obj.robot.encoders(1).ticks_per_rev;
            
            d_right = (right_ticks-prev_right_ticks)*m_per_tick;
            d_left = (left_ticks-prev_left_ticks)*m_per_tick;
            d_center = (d_right + d_left)/2;
            
            x_dt = d_center*cos(theta);
            y_dt = d_center*sin(theta);
            theta_dt = (d_right - d_left)/L;
            
            theta_new = theta + theta_dt;
            x_new = x + x_dt;
            y_new = y + y_dt;                           
%             fprintf('Estimated pose (x,y,theta): (%0.3g,%0.3g,%0.3g)\n', x_new, y_new, theta_new);
            
            % Save the wheel encoder ticks for the next estimate
            obj.prev_ticks.right = right_ticks;
            obj.prev_ticks.left = left_ticks;
            
            % Update your estimate of (x,y,theta)
            obj.state_estimate.set_pose([x_new, y_new, theta_new]);
        end
    end
end
