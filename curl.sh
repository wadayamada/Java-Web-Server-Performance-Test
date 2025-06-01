for i in {1..10000};
do echo "Request #$i"
curl -s  localhost:8081
done
