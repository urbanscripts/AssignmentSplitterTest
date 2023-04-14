echo "Deploying hosts..."

docker-compose -f docker-compose.yaml up -d 

echo "Tear down..."

docker-compose -f docker-compose.yaml down