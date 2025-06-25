CREATE TABLE accident_rule (
    accident_id INT REFERENCES accident(id) ON DELETE CASCADE,
    rule_id INT REFERENCES rule(id) ON DELETE CASCADE,
    PRIMARY KEY (accident_id, rule_id)
);