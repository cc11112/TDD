#!/bin/bash

server=${1-localhost:8085}

curl -s -H 'Content-Type:application/json' -d '["one"]' http://$server/checkclearing

history=`curl http://$server/checkclearing`

echo 'history data:'
echo $history

response=`curl -H 'Content-Type:application/json' -d "$history" http://$server/checkclearing`


#!["three and 0/100","FIVE","seven and 7/100","fourty four and 5/100","seventy three and 81/100","23/100","two","Two","three","five and 32/100","six","SIX","seven","eighteen and 78/100","twelve","ninety","SEVEN","eight","twenty five and 23/100","thirty one and 54/100","fifty five and 12/100","four dollars and 55/100","eighty eight","one","One","EIGHT","nine","sixty seven and 39/100","ninety nine and 99/100","four","one and 11/100","five","NINE","Three","Four","nine and 100/100"]' http://$server/checkclearing`

echo $response

if [ "$response" != '{"one":100}' ]; then

		echo "Test Failed!!"
		exit 1
else
	echo "Test OK"
fi










