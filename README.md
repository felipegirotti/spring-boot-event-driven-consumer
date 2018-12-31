# SEARCH MICROSERVICE - Event Driven with SpringBoot and RabbitMQ or SNS Consumer
This is a simple example of microservice, we are using the event driven architecture, this is a typical consumer.

The master branch uses RabbitMQ, the branch `sns-1` we change the broker to sns instead rabbitmq, see more details [here](#using-snssqs).

For more details of producer, see the example here: [https://github.com/felipegirotti/spring-boot-event-driven-producer](https://github.com/felipegirotti/spring-boot-event-driven-producer)

## gRPC   
We expose the data via [gRPC](https://grpc.io/). The [protofile](src/main/proto/search.proto) 

## Using SNS/SQS
To more details about how SNS works [click here](https://aws.amazon.com/sns/)   
First, change the branch for `sns-1`       
   
You need setup into AWS:
 - create a SNS topic
 - create a SQS to subscribe the SNS
 - create a user with the permission to send to SNS
 
 Use your credentials into the proper env vars and the topic name
 ```bash
 SQS_QUEUE_NAME={{YOUR_QUEUE_NAME}}
 AWS_CREDENTIAL_KEY_ID={{YOUR_KEY_ID}}
 AWS_CREDENTIAL_SECRET={{YOUR_SECRET}}
 AWS_REGION_STATIC={{YOU_REGION}}
 ```
 The region should be capitalize e.g `US-WEST-2`

## Run local
We are using env variables to setup the database properties and others configs  
See the .env.example and export your variables.  
e.g:    
```bash
export $(cat .env.example | xargs)
```

## ES
Some configurations is necessary before (Mappings and Enable to write in the ElasticSearch)

### Mappings

### Client
PUT /clients
```json
{
       "mappings": {
           "client": {
               "properties": {
                     "id": {"type": "long"},
                     "location": {"type": "geo_point"},
                     "name": { 
                        "type": "text",
                        "fields": {
                           "keyword": {
                               "type": "keyword",
                               "ignore_above": 256
                           }
                       }
                     }
                }
           }
       }
   }
```

### Places
PUT /places
```json
{
       "mappings": {
           "place": {
               "properties": {
                     "id": {"type": "long"},
                     "name": { "type": "text" },
                     "client_id": {"type": "long"},
                     "location": {"type": "geo_point"}
                }
           }
       }
   }
```

### Enable delete
PUT /_settings
```json
{
    "index": {
      "blocks": {
        "read_only_allow_delete": "false"
       }
    }
}
```

### Sample of places to ES
PUT /_bulk

```json
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "1" } }
{"id": 1, "name":"DRZ Geotecnologia e Consultoria","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1658525}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "2" } }
{"id": 2,"name":"Itau agencia higienopolis","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1763226}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "3" } }
{"id": 3, "name":"Banco do Brasil Estilo","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1667319}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "4" } }
{"id": 4, "name":"Caixa Ecomonica Federal","client_id":1,"location":{"lat":-23.3114913,"lon":-51.1660693}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "5" } }
{"id": 5, "name":"Bradesco Minas Gerais","client_id":1,"location":{"lat":-23.3116208,"lon":-51.1572625}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "6" } }
{"id": 6, "name":"Banco Do Brasil Mato Grosso","client_id":1,"location":{"lat":-23.3116208,"lon":-51.1572625}}
```

### Search
GET /places/place/_search?size=10
```json
{
    "query": {
        "bool" : {
            "must" : {
                "match_all" : {}
            },
            "filter" : {
                "geo_distance" : {
                    "distance" : "500m",
                    "location" : {
                        "lat" : -23.3103803,
                        "lon" : -51.1658525
                    }
                }
            }
        }
    }
}

```