### Task 1

```sh  
docker exec -it kafka sh
```  

```sh  
kafka-console-producer --broker-list localhost:9092 --topic task1-1
```  

```sh  
kafka-console-consumer --bootstrap-server localhost:9092 --topic task1-2 --from-beginning
```  

```text
Just a test message.
```

---  

### Task 2

```sh  
docker exec -it kafka sh
```  

```sh  
kafka-console-producer --broker-list localhost:9092 --topic task2
```  

```sh  
kafka-console-consumer --bootstrap-server localhost:9092 --topic task2-output --from-beginning
```  

```text  
Hello world, this is an example sentence.  
```  

```text  
Another example with longer words such as Abracadabra!  
```  

  
---  

### Task 3

```sh  
docker exec -it kafka sh
```  

```sh  
kafka-console-producer --broker-list localhost:9092 --topic task3-1
```  

```sh  
kafka-console-producer --broker-list localhost:9092 --topic task3-2
```  

```text  
1:Hello from task3-1  
```  

```text  
1:Response from task3-2  
```  

```text  
2:Testing join operation  
```  

```text  
2:Join me if you can  
```  

  
---  

### Task 4

```sh  
docker exec -it kafka sh
```  

```sh  
kafka-console-producer --broker-list localhost:9092 --topic task4
```  

```sh  
kafka-console-consumer --bootstrap-server localhost:9092 --topic task4-output --from-beginning
```  

```json  
{
  "name": "Giorgi",
  "company": "EPAM",
  "position": "developer",
  "experience": 5
}  
```  

```json  
{
  "name": "Nino",
  "company": "EPAM",
  "position": "manager",
  "experience": 10
}  
```  

```text  
You won't be able to deserialize me.  
```