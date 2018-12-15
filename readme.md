通常而言，编写一个YARN Appcalition涉及到3个RPC协议。分别为：

1）  ClientRMProtocol(Client<–>ResourceManager)

Client通过该协议将应用程序提交到ResourceManager上、查询应用程序的执行状态或者杀死应用程序等。

2）  AMRMProtocol(ApplicationMaster<–>ResourceManager)

ApplicationMaster使用该协议向ResourceManager注冊、申请资源以执行自己的各个任务。

3）  ContainerManager(ApplicationMaster<–> NodeManager)

ApplicationMaster使用该协议要求NodeManager启动/撤销Container，或者获取各个container的执行状态。