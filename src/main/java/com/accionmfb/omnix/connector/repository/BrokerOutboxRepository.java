package com.accionmfb.omnix.connector.repository;

import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import com.accionmfb.omnix.connector.model.BrokerOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class BrokerOutboxRepository {

    private final JdbcTemplate jdbcTemplate;

    public BrokerOutbox saveAndFlush(BrokerOutbox brokerOutbox){
        String query = SqlQueryProvider.getOutboxSaveQuery(brokerOutbox);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, brokerOutbox.getTopic());
            ps.setString(2, brokerOutbox.getMessageKey());
            ps.setString(3, brokerOutbox.getPayload());
            ps.setTimestamp(4, Timestamp.valueOf(brokerOutbox.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(brokerOutbox.getUpdatedAt()));
            ps.setString(6, brokerOutbox.getStatus().name());
            return ps;
        }, keyHolder);
        brokerOutbox.setId(keyHolder.getKey().longValue());
        return brokerOutbox;
    }

    public void updateBrokerOutbox(BrokerOutbox brokerOutbox){
        String query = SqlQueryProvider.getOutboxUpdateQuery(brokerOutbox);
        jdbcTemplate.update(query);
    }

    public void deleteBrokerOutbox(BrokerOutbox brokerOutbox){
        String query = SqlQueryProvider.getDeleteOutboxQuery(brokerOutbox.getId());
        jdbcTemplate.execute(query);
    }

    public List<BrokerOutbox> findAllByStatus(BrokerMessageDeliveryStatus status){
        String query = SqlQueryProvider.getFetchAllBrokerOutboxByStatus(status);
        return jdbcTemplate.query(query, getBrokerOutboxRowMapper());
    }

    public RowMapper<BrokerOutbox> getBrokerOutboxRowMapper(){
        return ((rs, rowNum) -> BrokerOutbox.builder()
                .id((long) rowNum)
                .topic(rs.getString("topic"))
                .messageKey(rs.getString("message_key"))
                .payload(rs.getString("payload"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .status(BrokerMessageDeliveryStatus.valueOf(rs.getString("status")))
                .build());
    }

}
