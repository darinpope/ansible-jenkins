# loaded

The controller needs extra HDD. To do that, first run:

vagrant plugin install vagrant-disksize

then

vagrant up

then ssh into the box and

sudo cfdisk /dev/sda

sudo xfs_growfs /dev/sda2

https://stackoverflow.com/a/60185312

