#!/bin/bash

server=${1-localhost:8085}

function cleanup {
	kill $server_pid
	rm gradle.properties
}

trape "cleanup" INT TERM EXIT

function unknown_files {
	unknown_file_count=`git status --porcelain | grep "^??" | wc -l`
	[[ "$unknown_file_count" -gt 0 ]]
}

function uncommited_changes_ {
	changes_commited=`git diff HEAD --shortstat | wc -l `
	[[ "$changes_commited" -gt 0 ]]
}

if unknown_files; then
	echo 'unknown files in project'
	exit 1
fi

if uncommited_changes_; then
	echo 'Uncommited files in project'
	exit 1
fi 

gradle clean build 
if [ "$?" -gt 0 ] ; then
	exit 1
fi

gradle gaeRun &
server_pid=$!
if [ "$server_pid" -gt 0 ] ; then
	echo "Server failed to start"
	exit 1
fi

echo -n "Waiting for local server to start..."
server_status=1
while [ ! "$server_status" -gt 0 ] ; do
	echo -n .
	echo http://$server/checkclearing
	server_status=$?
	sleep 1
done


curl -s -H 'Content-Type:application/json' -d '["one"]' http://$server/checkclearing

history=`curl http://$server/checkclearing`

echo 'history data:'
echo $history

echo $history | python -mjson.tool > /dev/null
if [ "$?" -eq 0 ]; then
	echo "Response is valid JSON"
else
	echo "Response is NOT valid JSON"
	echo $history
	kill $server_pid
	exit 1	
fi

kill $server_pid

echo "Build successful! Enter AppEnigner password to deploy"
stty -echo
read -p "Password: " password
echo
stty echo
echo "gaePassword=$password" > gradle.properties
#! gradle gaeUpload
rm gradle.properties


timestamp=`date "+%Y-%m-%d %H:%M:%S"`
git tag DEPLOY_$timestamp
echo $timestamp


echo 'exit...'
exit 0


response=`curl -H 'Content-Type:application/json' -d "$history" http://$server/checkclearing`

echo $response

if [ "$response" != '{"one":100}' ]; then

		echo "Test Failed!!"
		exit 1
else
	echo "Test OK"
fi









