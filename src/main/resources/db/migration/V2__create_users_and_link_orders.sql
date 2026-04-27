CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE
);

-- Create placeholder users for existing orders so the FK can be added safely.
INSERT INTO users (id, full_name, email)
SELECT DISTINCT o.user_id,
       'User ' || o.user_id,
       'user' || o.user_id || '@local.test'
FROM orders o
WHERE o.user_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM users u WHERE u.id = o.user_id
  );

SELECT setval(
    pg_get_serial_sequence('users', 'id'),
    COALESCE((SELECT MAX(id) FROM users), 1),
    true
);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_users
    FOREIGN KEY (user_id)
    REFERENCES users(id);
