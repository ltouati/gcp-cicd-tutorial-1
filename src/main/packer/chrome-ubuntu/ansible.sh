#!/bin/bash -eux
# Install Ansible repository and Ansible.
apt -y install software-properties-common
apt-add-repository ppa:ansible/ansible
apt-get update -y
apt-get install -y ansible default-jdk
