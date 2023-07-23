INSERT INTO conta (id_conta, nome_responsavel) VALUES (4,'Angular');
INSERT INTO conta (id_conta, nome_responsavel) VALUES (5,'React');

INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (8,'2020-05-04 08:12:45+03',-500.50,'SAQUE', null, 5);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (9,'2021-08-07 08:12:45+03',530.50,'DEPOSITO', null, 4);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (10,'2022-06-08 10:15:01+03',3241.23,'TRANSFERENCIA', 'Fulano', 4);
INSERT INTO transferencia (id,data_transferencia, valor, tipo, nome_operador_transacao, conta_id) VALUES (11,'2023-04-01 12:12:04+03',25173.09,'TRANSFERENCIA', 'Sicrano', 5);
