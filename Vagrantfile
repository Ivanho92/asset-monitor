# Vagrantfile - provisions the Ubuntu VM used to run Asset Monitor.
#
# Run `vagrant up` from this directory. This single command creates the VM, installs
# Docker and Git inside it, clones this project from GitHub, and starts it with
# Docker Compose. No manual setup steps required.
#
# Note: replace REPO_URL below with your actual GitHub repo URL once it exists.

REPO_URL = "https://github.com/Ivanho92/asset-monitor.git"

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64" # Ubuntu 22.04 LTS
  config.vm.hostname = "asset-monitor-vm"

  config.vm.provider "virtualbox" do |vb|
    vb.name = "asset-monitor-vm"
    vb.memory = 4096
    vb.cpus = 2
  end

  config.vm.network "forwarded_port", guest: 4200, host: 4200 # Angular
  config.vm.network "forwarded_port", guest: 8080, host: 8080 # Spring Boot

  # No synced folder -- code reaches the VM via git clone, not a live host mount.
  config.vm.synced_folder ".", "/vagrant", disabled: true

  config.vm.provision "shell", inline: <<-SHELL
    set -e

    apt-get update
    apt-get install -y ca-certificates curl gnupg git

    install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    chmod a+r /etc/apt/keyrings/docker.gpg

    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      tee /etc/apt/sources.list.d/docker.list > /dev/null

    apt-get update
    apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
    usermod -aG docker vagrant

    if [ ! -d /home/vagrant/asset-monitor ]; then
      git clone #{REPO_URL} /home/vagrant/asset-monitor
    else
      cd /home/vagrant/asset-monitor && git pull
    fi

    cd /home/vagrant/asset-monitor
    docker compose up --build -d

    echo "Stack is up. Visit http://localhost:4200 and http://localhost:8080/api/health from your host."
  SHELL
end
