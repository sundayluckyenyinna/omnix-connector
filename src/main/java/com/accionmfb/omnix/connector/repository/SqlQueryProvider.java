package com.accionmfb.omnix.connector.repository;

import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import com.accionmfb.omnix.connector.model.BrokerOutbox;
import com.accionmfb.omnix.connector.util.ConnectorUtils;
import org.apache.logging.log4j.util.Strings;

public class SqlQueryProvider {

    public static String getOutboxSaveQuery(BrokerOutbox outbox){
        return "INSERT INTO broker_outbox (topic, message_key, payload, created_at, updated_at, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    public static String getOutboxUpdateQuery(BrokerOutbox outbox){
        String query = "UPDATE broker_outbox SET updated_at = '%s', status = '%s' WHERE id = %s";
        return String.format(query, outbox.getUpdatedAt(), outbox.getStatus(), outbox.getId());
    }

    public static String getDeleteOutboxQuery(Long id){
        String query = "DELETE FROM broker_outbox WHERE id = %s";
        return String.format(query, id);
    }

    public static String getFetchAllBrokerOutboxByStatus(BrokerMessageDeliveryStatus status){
        String query = "SELECT * FROM broker_outbox WHERE status = '%s'";
        return String.format(query, status);
    }
}
