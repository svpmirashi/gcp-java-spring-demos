# gcp-java-spring-demos

Run consumer A:

<code>java -jar target\gcp-pubsub-poc-1.0.0.jar --gcp.pubsub.consumerName=CON-A --gpc.pubsub.subscriptionName=rms-sub-ordered --server.port=8088</code>

Run consumer B:

<code>java -jar target\gcp-pubsub-poc-1.0.0.jar --gcp.pubsub.consumerName=CON-B --gpc.pubsub.subscriptionName=rms-sub-ordered --server.port=8089</code>

Publish messages using gcloud cli:

<code>gcloud pubsub topics publish rms-trade-poc-topic-1 --message="Message body T1 V1" --attribute="trade_id=T1,version=V1" --ordering-key=trade_id</code>
