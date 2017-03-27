# CloudSystemDocker
Developed a cloud system that creates Virtual Machines (VM) using Docker containers with features such as Load balancing, Fault tolerance, Authentication, and Security. Developed Wikipedia like application which used this cloud system with MongoDB database.


Problem Statement:
Develop a cloud system that creates VMs (or applications) per user request and balances load
among physical servers. As users send requests for VMs, the cloud finds a target physical
machine for each request, and starts a VM as a process on the target machine. Docker VM has
been installed on some CS machines (see list at the end of the document). You are free to use any
remote execution techniques like RMI, sockets, or SSH-based execution. You may use eight CS
machines as cloud servers. Each client issues a request for executing jobs as described in the
problems. Your system should allocate resources (Virtual Machines) to jobs. You can base the
allocation on such criteria as complexity of the job, number of users, priority etc. Please explain
which criteria you are using and give a justification. Each problem implementation must include
a comprehensive implementation of one or more features. The outcome of each problem
implementation should result in better utilization of resources and improved performance. Please
show results. Each experimental reading should be repeated at least 19 times (for 95%
confidence in your result) and show the average and standard deviation.

Features
1. Load Balancing and Migration
The load on the number of physical servers must be balanced most of the time. Describe
and cite the load balancing scheme you are using. Dynamic load balancing entails
migration of jobs from one VM to another (as well as migration of VMs among physical
machines). You will be required to give a complete profile of server usage.
2. Security
Each individual job must be isolated. Show that if there is an attack (e.g., DoS) on one
physical server or VM, the others are unaffected. Add other features such as encryption.
3. Fault-tolerance
The system must continue to provide cloud services when faults take place. The
remaining (non-faulty) systems should take over the job.


Develop a Wikipedia like service for RIT/Rochester, mainly providing information for students.
This will be useful for future students. Tens of students contribute and utilize the Wikipedia
service. The service should provide functions for three kinds of users – contributors, editors, and
readers. Each kind of user should have different privileges. You may use existing tools for
spellcheck, language etc.
