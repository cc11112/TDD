#!/bin/bash

server=${1-localhost:8085}

response=`curl -H Content-Type:application/json -d '["one"]' http://$server/checkclearing`

if [ $response != '{"one":100}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["four and 56/100"]' http://$server/checkclearing`

if [ $response != '{"four and 56/100":456}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi



response=`curl -H Content-Type:application/json -d '["ninety nine and 99/100"]' http://$server/checkclearing`

if [ $response != '{"ninety nine and 99/100":9999}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["twenty]' http://$server/checkclearing`

if [ $response != '{"twenty":2000}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


echo "TEST Succeeded."










