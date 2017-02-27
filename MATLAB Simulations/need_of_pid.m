close all;
clear all;
clc;
% This program demonstates the need for PID controllers
%% Attempt 1 (Bang Bang Control)
%u=u_max, error>0 (r>y)
%u=u_min, error<0 (r<y)
%where r is the reference signal to be tracked, u is the control signal
%and y is the measured output for the cruise controller, x_dot=(c/m)*u where
%x is the state of the system, x=velocity of the robot.
u_max=100; %max acceleration possible
u_min=-100; %max deceleration possible due to application of brakes
x=0; %the system starts from rest
y=x; %measured output speed
r=70;%reference signal to be tracked
C=1; %ratio of c/m
u=[];
X=[];
T=[];
del_t=0.01;
for t=0:del_t:20
    if r-y>0
        u_i=u_max;
    elseif r-y<0
        u_i=u_min;
    end
    x=x+u_i*del_t;
    y=x;
    X=[X x];
    u=[u u_i];
    T=[T t];
end
figure;
subplot(2,1,1);
plot(T,X);
subplot(2,1,2);
plot(T,u);
%% Attempt 2 (P-regulator)
x_p=0;
K_p=0.6; % Proportional gain
gamma=0.05; %air resistance coefficient
y_p=x_p;
e_p=r-y_p;
u_p=K_p*e_p;
X_p=[];
U_p=[];
T_p=[];
for t_p=0:del_t:20
    e_p=r-y_p
    u_p=K_p*e_p;
    x_p=x_p+u_p*del_t-gamma*x_p*del_t;
    y_p=x_p;
    X_p=[X_p x_p];
    T_p=[T_p t_p];
    U_p=[U_p u_p];
end
figure;
subplot(2,1,1);
plot(T_p,X_p);
subplot(2,1,2);
plot(T_p,U_p);

%% Attempt 3(PI regulator)
x_pi=0;
K_i=2;%integral gain
y_pi=x_pi;
t_pi=0;
e_old=r-x_pi; % no error for reference (=0)
u_pi=K_p*e_old;
x_pi=x_pi+u_pi*del_t-gamma*x_pi*del_t;
X_pi=[x_pi];
U_pi=[u_pi];
T_pi=[t_pi];
for t_pi=0.01:del_t:20;
    e_pi=r-x_pi;
    E_pi=e_pi+e_old;
    e_old=E_pi;
    u_pi=K_p*e_pi+K_i*E_pi*del_t;
    x_pi=x_pi+u_pi*del_t-gamma*x_pi*del_t;
    X_pi=[X_pi x_pi];
    T_pi=[T_pi t_pi];
    U_pi=[U_pi u_pi];
end
figure;
subplot(2,1,1);
plot(T_pi,X_pi);
subplot(2,1,2);
plot(T_pi,U_pi);

%% Attempt 4(PID regulator)
x_pid=0;
K_d=0.6;
y_pid=x_pid;
t_pid=0;
e_old_pid=r-x_pid; % no error for reference (=0)
u_pid=K_p*e_old_pid;
x_pid=x_pid+u_pid*del_t-gamma*x_pid*del_t;
X_pid=[x_pid];
U_pid=[u_pid];
T_pid=[t_pid];
e_oldy=e_old_pid;
for t_pid=0.01:del_t:20
    e_pid=r-x_pid;
    E_pid=e_pid+e_old_pid;
    e_dot=e_oldy-e_pid;
    e_old_pid=E_pid;
    e_oldy=e_pid;
    u_pid=K_p*e_pid+K_i*E_pid*del_t+K_d*e_dot/del_t;
    x_pid=x_pid+u_pid*del_t-gamma*x_pid*del_t;
    X_pid=[X_pid x_pid];
    T_pid=[T_pid t_pid];
    U_pid=[U_pid u_pid];
end
figure;
subplot(2,1,1);
plot(T_pid,X_pid);
subplot(2,1,2);
plot(T_pid,U_pid);