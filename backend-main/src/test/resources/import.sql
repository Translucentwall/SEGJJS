insert into authors (id,au_name) values (1,'a'),(2,'b'),(3,'c');
insert into affiliations (id, af_name) values (1,'nju'),(2,'google');
insert into conferences (id,c_name) values (1,'ase'),(2,'icse');
insert into terms (id,content) values (1,'data'),(2,'mining');
insert into papers (id,title,conference_id) values (1,'data data',1),(2,'mining',2),(3,'data mining',1);
insert into paper_aa (paper_id, affiliation_id, author_id) values (1,1,1),(2,2,2),(3,2,3),(3,1,1);
insert into papers_author_keywords (paper_id, author_keywords_id) values (1,1),(2,2),(3,1),(3,2);
insert into author_affiliation_year (id,author_id,affiliation_id,`year`) values (1,1,1,2012),(2,1,2,2020),(3,2,1,2012);
insert into aa_cooperate(id,`year`,author1_id,author2_id) values (1,2016,1,2), (2,2017,1,3);