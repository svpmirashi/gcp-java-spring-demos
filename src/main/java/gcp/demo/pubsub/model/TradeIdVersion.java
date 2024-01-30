package gcp.demo.pubsub.model;

import lombok.Builder;

@Builder
public record TradeIdVersion(String tradeId, String tradeVersion) {
    public static final String TRADE_ID = "trade_id";
    public static final String TRADE_VERSION = "trade_version";

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(tradeId());
        builder.append("--");
        builder.append(tradeVersion);
        return builder.toString();
    }
}
