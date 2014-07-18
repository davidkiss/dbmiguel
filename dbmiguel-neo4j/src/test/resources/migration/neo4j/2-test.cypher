--@type=cypher
--@author=dkiss

-- this is a comment
start n=node(*) where n.name = 'my node' return n, n.name