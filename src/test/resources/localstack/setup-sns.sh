#!/usr/bin/env bash
set -e
export TERM=ansi
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar
export AWS_DEFAULT_REGION=eu-west-2
export PAGER=

printenv

aws --endpoint-url=http://localhost:4575 sns create-topic --name offender_assessments_events
aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name test_queue
aws --endpoint-url=http://localhost:4575 sns subscribe \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_assessments_events \
    --protocol sqs \
    --notification-endpoint http://localhost:4576/queue/test_queue

aws --endpoint-url=http://localhost:4575 sns publish --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_assessments_events --message "TEST"

echo "SNS Configured"
