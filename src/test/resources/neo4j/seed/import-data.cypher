MATCH (n)-[r]-() DELETE n, r;
MATCH (n) DELETE n;

CALL apoc.load.json("initialData.json") YIELD value AS data
UNWIND data.nodes AS nodeData

MERGE (node:StarSystem {id: nodeData.properties.id})
SET node += apoc.map.clean(nodeData.properties, ['label'], [])

WITH nodeData, node, data
UNWIND data.relationships AS relData

MATCH (start:StarSystem {name: relData.start}), (end:StarSystem {name: relData.end})
MERGE (start)-[r:TRAVEL_TO]->(end)
SET r += relData.properties;