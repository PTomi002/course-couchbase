# hu.ptomi.course.couchbase.CouchBase Training

## Start Infra

`docker-compose -f docker-compose.couchbase.yml up -d`<br />
`docker compose -f docker-compose.couchbase.yml stop`<br />

`docker-compose -f docker-compose.couchbase.yml rm`<br />
`docker-compose -f docker-compose.couchbase.yml restart`<br />

`docker-compose -f docker-compose.couchbase.yml logs`<br />

Add hosts to cluster (couchbase_3 is available because I opened the ports at that image):<br />
`docker inspect couchbase_1`<br />
`docker inspect couchbase_2`<br />
Click Rebalance button.<br />

`docker exec -it couchbase_1`

## Cluster Setup
`user: admin pwd: password`<br />
`see pictures in the main/resources filder`<br />
