package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDaoImpl implements CardDao {
    private final Connection connection;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public CardDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Card save(Card card) {
        try {
            QueryResult queryResult = queryBuilderUtil.createInsertQuery("card", card);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return card;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Card> findById(String id) {
        String sql = "SELECT * FROM card WHERE card_no = ?";
        return getCard(id, sql);
    }

    @Override
    public List<Card> findAll() {
        String sql = "SELECT * FROM card";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Card> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(mapResultSetToCard(rs));
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Card update(Card card) {
        try {
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("card", card, "card_no", card.getCardNo());
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return card;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM card WHERE card_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> findByUserId(Long userId) {
        String sql = "SELECT * FROM card WHERE user_id = ?";
        return getCardsBy(userId, sql);
    }

    @Override
    public List<Card> findByAccNo(String accNo) {
        String sql = "SELECT * FROM card WHERE acc_no = ?";
        return getCardsBy(accNo, sql);
    }

    private <T> List<Card> getCardsBy(T arg, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (arg instanceof Long) {
                stmt.setLong(1, (Long) arg);
            } else {
                stmt.setString(1, (String) arg);
            }
            ResultSet rs = stmt.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(mapResultSetToCard(rs));
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Optional<Card> getCard(T arg, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (arg instanceof Long) {
                stmt.setLong(1, (Long) arg);
            } else {
                stmt.setString(1, (String) arg);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCard(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setUserId(rs.getLong("user_id"));
        card.setCardNo(rs.getString("card_no"));
        card.setCardCategory(CardCategory.valueOf(rs.getString("card_category")));
        card.setStatus(CardStatus.valueOf(rs.getString("status")));
        card.setAtmPinHashed(rs.getString("atm_pin_hashed"));
        card.setCvvHashed(rs.getString("cvv_hashed"));
        card.setValidFrom(Date.valueOf(rs.getDate("valid_from").toLocalDate()));
        card.setValidTill(Date.valueOf(rs.getDate("valid_till").toLocalDate()));
        return card;
    }
}
