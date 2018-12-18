# SEARCH MICROSERVICE

## Run local
We are using env variables to setup the database properties and others configs  
See the .env.example and export your variables.  
e.g:    
```bash
export $(cat .env.example | xargs)
```

## ES
### Mappings
PUT /places
```json
{
       "mappings": {
           "place": {
               "properties": {
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
{"name":"DRZ Geotecnologia e Consultoria","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1658525}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "3" } }
{"name":"Itau agencia higienopolis","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1763226}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "4" } }
{"name":"Banco do Brasil Estilo","client_id":1,"location":{"lat":-23.3103803,"lon":-51.1667319}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "5" } }
{"name":"Caixa Ecomonica Federal","client_id":1,"location":{"lat":-23.3114913,"lon":-51.1660693}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "6" } }
{"name":"Bradesco Minas Gerais","client_id":1,"location":{"lat":-23.3116208,"lon":-51.1572625}}
{ "index" : { "_index" : "places", "_type" : "place", "_id" : "7" } }
{"name":"Banco Do Brasil Mato Grosso","client_id":1,"location":{"lat":-23.3116208,"lon":-51.1572625}}
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