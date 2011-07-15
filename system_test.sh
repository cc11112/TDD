#!/bin/bash

server=${1-localhost:8085}

response=`curl -H Content-Type:application/json -d '["one"]' http://$server/checkclearing`
echo $response
if [ $response != '{"one":100}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["four and 56/100"]' http://$server/checkclearing`
echo $response
if [ $response != '{"four and 56/100":456}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["eight"]' http://$server/checkclearing`
echo $response
if [ $response != '{"eight":800}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["one","two and 50/100","three dollars"]' http://$server/checkclearing`
echo $response
if [ $response != '{"one":100,"two and 50/100":250,"three dollars":300}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["one","two and 50/100","red dollars"]' http://$server/checkclearing`
echo $response
if [ $response != '{"one":100,"two and 50/100":250,"red dollars":300}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["ninety nine and 99/100"]' http://$server/checkclearing`
echo $response
if [ $response != '{"ninety nine and 99/100":9999}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["twenty"]' http://$server/checkclearing`
echo $response
if [ $response != '{"twenty":2000}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["fifty eight and 31/100"]' http://$server/checkclearing`
echo $response
if [ $response != '{"fifty eight and 31/100":5831}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi

response=`curl -H Content-Type:application/json -d '["fifty eight and 100/100"]' http://$server/checkclearing`
echo $response
if [ $response != '{"fifty eight and 100/100":5900}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi


response=`curl -H Content-Type:application/json -d '["ninety nine and abc/100"]' http://$server/checkclearing`
echo $response
if [ $response != '{}' ] ; then
	echo "TEST FAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
fi



echo "TEST Succeeded."










