CREATE TABLE LC_Agency (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,

                           name            VARCHAR(255)      NOT NULL,
                           email           VARCHAR(255),
                           phone           VARCHAR(50),

                           remarks         TEXT,

                           address         VARCHAR(255),
                           bill_address    VARCHAR(255),
                           city            VARCHAR(100),
                           state           VARCHAR(100),
                           postal          VARCHAR(20),

                           enrollment      VARCHAR(100),
                           website         VARCHAR(255),

                           logo            LONGBLOB,        -- stores base64 decoded binary
                           tier            VARCHAR(50),

                           shipping_acc    VARCHAR(100),
                           EIN             VARCHAR(50),

                           EXT1           VARCHAR(255),
                           EXT2           VARCHAR(255),
                           EXT3           VARCHAR(255),
                           EXT4           VARCHAR(255),
                           EXT5           VARCHAR(255),


                           make_time       DATETIME         DEFAULT CURRENT_TIMESTAMP,
                           modify_time     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE LC_Agent (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,

                          employ_id      VARCHAR(100)      NOT NULL,
                          company_id     VARCHAR(100),
                          title          VARCHAR(255),

                          logo           LONGBLOB,          -- for binary logo storage
                          address        VARCHAR(255),

                          currency       VARCHAR(20),
                          remark         TEXT,

                          phone          VARCHAR(50),
                          office_num     VARCHAR(50),
                          phone_ext      VARCHAR(20),

                          EXT1           VARCHAR(255),
                          EXT2           VARCHAR(255),
                          EXT3           VARCHAR(255),
                          EXT4           VARCHAR(255),
                          EXT5           VARCHAR(255),

                          make_time      DATETIME DEFAULT CURRENT_TIMESTAMP,
                          modify_time    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE LC_Agent
    ADD CONSTRAINT fk_agent_company
        FOREIGN KEY (company_id)
            REFERENCES LC_Agency(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;
