#!/bin/bash

## setup_RCCarGPIO.sh - A script to prepare GPIO for controlling an RC car using a Pololu DRV8835 controller ##

# Setup GPIO for power control
gpio -g mode 12 pwm
gpio -g mode 13 pwm
gpio pwmc 2
gpio pwmr 480
gpio pwm-ms
gpio -g pwm 12 0
gpio -g pwm 13 0

# Setup GPIO for direction control
gpio -g mode 5 out
gpio -g mode 6 out
gpio -g write 5 0
gpio -g write 6 0

exit 0
