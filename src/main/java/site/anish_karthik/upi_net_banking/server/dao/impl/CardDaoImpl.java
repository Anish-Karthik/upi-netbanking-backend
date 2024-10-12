package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

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

    public CardDaoImpl() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
                cards.add(ResultSetMapper.mapResultSetToObject(rs, Card.class));
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
                cards.add(ResultSetMapper.mapResultSetToObject(rs, Card.class));
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
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Card.class));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateManyByAccNo(Card card, String accNo) {
        try {
            System.out.printf("Card::: Updating cards for account %s\n", accNo);
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("card", card, "acc_no", accNo);
            System.out.println("Card::: Executing query:"+ queryResult);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            System.out.println("Cards updated");
        } catch (IllegalAccessException | SQLException e) {
            System.out.printf("Error updating cards for account %s\n", accNo);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<BankAccount> getAccountDetails(String cardNo) {
        String sql = "SELECT * FROM bank_account WHERE acc_no = (SELECT acc_no FROM card WHERE card_no = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cardNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, BankAccount.class));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
