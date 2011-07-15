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

response=`curl -H Content-Type:application/json -d '["eight]' http://$server/checkclearing`

if [ $response != '{"eight":800}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["one","two and 50/100","three dollars"]' http://$server/checkclearing`

if [ $response != '{"one":100,"two and 50/100":250,"three dollars":300}' ] ; then
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


response=`curl -H Content-Type:application/json -d '["twenty"]' http://$server/checkclearing`

if [ $response != '{"twenty":2000}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["fifty eight and 31/100"]' http://$server/checkclearing`

if [ $response != '{"fifty eight and 31/100":5831}' ] ; then
	echo $response
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi



echo "TEST Succeeded."










