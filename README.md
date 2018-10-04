# Purpose
This repo contains the code of the demo presented during the talk [Exploring the Full Potential of Ignite Using Classless Design](https://www.imcsummit.org/2018/us/session/exploring-full-potential-ignite-using-classless-design)
at the In Memory Computing Summit, north america 20018. For a better understanding, please watch the recording of the talk.  
It is not a final product, it is mainly used as proof of concept.
# How to run
## Setup
First build your project using mvn:  
``` mvn clean install ```  
At any time, if you want to reset the data if the server nodes, shut them down and run a maven build

## Ignite server grid
Then, you will need to start an Ignite server grid. To do so, you can use the utility in [ignite-grid](ignite-grid).  
There are 3 runnable that will start 3 server nodes, just run the main method in:  
- [PServerNode1Starter](ignite-grid/src/main/java/com/ing/imc/grid/PServerNode1Starter.java)
- [PServerNode2Starter](ignite-grid/src/main/java/com/ing/imc/grid/PServerNode2Starter.java)
- [PServerNode3Starter](ignite-grid/src/main/java/com/ing/imc/grid/PServerNode3Starter.java)

This will start an Ignite grid, 2 servers are enough for a start.

## Initial data
You will need to create the initial caches and put in some data for testing. For this, you will have to look
into the [upgrade-client](upgrade-client). Run the java classes in the following order:
1) [CreateCaches](upgrade-client/src/main/java/com/ing/learn/imc/client/a_initial/CreateCaches.java)
2) [PopulateCustomerAndAccountData](upgrade-client/src/main/java/com/ing/learn/imc/client/a_initial/PopulateCustomerAndAccountData.java)

## Initial application
You can now start the application server (a). Just start the springboot [RestNodeA](rest-server-a/src/main/java/com/ing/imc/RestNodeA.java). 
You can then send rest requests. There is an example set of request in a Postman project in [IMC Summit Demo](IMC%20Summit%20Demo.postman_collection.json).

## Evolution
Now that the RestNodeA is running, we can simulate a new application coming in. First, we will need to create the new alert cache.
For this, run the main method in [CreateAlertCache](upgrade-client/src/main/java/com/ing/learn/imc/client/b_limits/CreateAlertCache.java).
Once the cache has been created, you can start the second server [RestNodeB](rest-server-b/src/main/java/com/ing/imc/RestNodeB.java) 
and start firing rest requests to the second server.
 
