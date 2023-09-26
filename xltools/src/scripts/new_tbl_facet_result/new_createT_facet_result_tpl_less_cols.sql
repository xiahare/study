--  
--   tmp_xl_pk_facet_result
--   (`start_time`,`hash_id`,`adomid`,`row_no`)

-- DROP TABLE IF EXISTS `${VAR:table_name}`;
CREATE TABLE IF NOT EXISTS `${VAR:table_name}`
(
		`start_time` timestamp not null,
		`row_no` bigint not null,
    `hash_id` bigint not null,
    `adomid` bigint not null,

	
		`slot` int not null,
		`end_time` timestamp not null,
    `int0` int,	
    `int1` int,	
    `int2` int,	
    `int3` int,	
    `int4` int,	
    `int5` int,	
    `int6` int,	
    `int7` int,	
    `int8` int,	
    `int9` int,	
    `int10` int,	
    `int11` int,	
    `int12` int,	
    `int13` int,	
    `int14` int,	
    `int15` int,	
    `int16` int,	
    `int17` int,	
    `int18` int,	
    `int19` int,	
    `int20` int,
    `bigint0` bigint,	
    `bigint1` bigint,	
    `bigint2` bigint,	
    `bigint3` bigint,	
    `bigint4` bigint,	
    `bigint5` bigint,	
    `bigint6` bigint,	
    `bigint7` bigint,	
    `bigint8` bigint,	
    `bigint9` bigint,	
    `bigint10` bigint,	
    `bigint11` bigint,	
    `bigint12` bigint,	
    `bigint13` bigint,	
    `bigint14` bigint,	
    `bigint15` bigint,	
    `bigint16` bigint,	
    `bigint17` bigint,	
    `bigint18` bigint,	
    `bigint19` bigint,	
    `bigint20` bigint,	
    `bigint21` bigint,	
    `bigint22` bigint,	
    `bigint23` bigint,	
    `bigint24` bigint,	
    `bigint25` bigint,	
    `bigint26` bigint,	
    `bigint27` bigint,	
    `bigint28` bigint,	
    `bigint29` bigint,	
    `bigint30` bigint,
    `float0` float,
    `float1` float,
    `float2` float,
    `float3` float,
    `float4` float,
    `float5` float,
    `float6` float,
    `float7` float,
    `float8` float,
    `float9` float,
    `float10` float,
    `string0` string,
    `string1` string,
    `string2` string,
    `string3` string,
    `string4` string,
    `string5` string,
    `string6` string,
    `string7` string,
    `string8` string,
    `string9` string,
    `string10` string,
    `string11` string,
    `string12` string,
    `string13` string,
    `string14` string,
    `string15` string,
    `string16` string,
    `string17` string,
    `string18` string,
    `string19` string,
    `string20` string,
    `string21` string,
    `string22` string,
    `string23` string,
    `string24` string,
    `string25` string,
    `string26` string,
    `string27` string,
    `string28` string,
    `string29` string,
    `string30` string,
    `string31` string,
    `string32` string,
    `string33` string,
    `string34` string,
    `string35` string,
    `string36` string,
    `string37` string,
    `string38` string,
    `string39` string,
    `string40` string,
    `string41` string,
    `string42` string,
    `string43` string,
    `string44` string,
    `string45` string,
    `string46` string,
    `string47` string,
    `string48` string,
    `string49` string,
    `timestamp0` timestamp,
    `timestamp1` timestamp,
    `timestamp2` timestamp,
    `timestamp3` timestamp,
    `timestamp4` timestamp,
    `timestamp5` timestamp,
    `timestamp6` timestamp,
    `timestamp7` timestamp,
    `timestamp8` timestamp,
    `timestamp9` timestamp,
    `double0` double,
    `double1` double,
    `double2` double,
    `double3` double,
    `double4` double,
    `double5` double,
    `double6` double,
    `double7` double,
    `double8` double,
    `double9` double,
    `boolean0` boolean,
    `boolean1` boolean,
    `boolean2` boolean,
    `boolean3` boolean,
    `boolean4` boolean,
    PRIMARY KEY(`start_time`,`row_no`,`hash_id`,`adomid`)
)
PARTITION BY HASH(hash_id,adomid) PARTITIONS 9, HASH(row_no) PARTITIONS 3 , RANGE (start_time) (PARTITION VALUES < '${VAR:init_partition}')
STORED AS KUDU TBLPROPERTIES ('kudu.num_tablet_replicas' = '3');
