#!/usr/bin/env bash
sudo apt-get update

echo "Installing Git.."
sudo apt-get install -y git

echo "Installing Maven.."
sudo apt-get install -y maven

echo "Installing Java 8.."
apt-get install --yes python-software-properties
add-apt-repository ppa:webupd8team/java
apt-get update -qq
echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
apt-get install --yes oracle-java8-installer
yes "" | apt-get -f install

echo "Install the PostgreSQL.."
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo echo "deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main 9.4" >> pgdg.list
sudo cp pgdg.list /etc/apt/sources.list.d/pgdg.list
sudo apt-get update
sudo apt-get -y install postgresql-9.4 postgresql-client-9.4

echo "Changing to dummy password.."
sudo pg_createcluster 9.4 test 
sudo vi /etc/postgresql/9.4/test/pg_hba.conf 
sudo -u postgres psql -c "ALTER USER postgres WITH ENCRYPTED PASSWORD 'postgres';"
sudo -u postgres psql -c "CREATE USER aleksandrbogomolov WITH ENCRYPTED PASSWORD 'password';"
# Edit to allow socket access, not just local unix access
echo "Patching pg_hba to change -> socket access"
sudo cp /etc/postgresql/9.4/test/pg_hba.conf .
sudo chmod a+rw pg_hba.conf
sudo sed 's/local.*all.*postgres.*peer/local all postgres md5/' < pg_hba.conf > pg_hba2.conf
sudo sed 's/local.*all.*all.*peer/local all all md5/' < pg_hba2.conf > pg_hba3.conf
echo "Altered login to use md5 not peer:"
sudo printf "\nhost    all             all             127.0.0.1/32            trust\n" >> pg_hba3.conf

cat pg_hba3.conf

sudo chmod u-rw pg_hba3.conf 
sudo cp pg_hba3.conf /etc/postgresql/9.4/test/pg_hba.conf
echo "Patching complete, restarting"

sudo /etc/init.d/postgresql restart

echo "Creating new DB.."
sudo -u postgres createdb -E UTF8 -O aleksandrbogomolov helloBackEnd