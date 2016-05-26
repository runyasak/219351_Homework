nodes1 = LOAD 'web-Google.txt' USING PigStorage('\t') AS (from1:chararray, to1:chararray);
nodes2 = LOAD 'web-Google.txt' USING PigStorage('\t') AS (from2:chararray, to2:chararray);
joining = JOIN nodes1 BY to1 LEFT OUTER, nodes2 By from2;
grouped = GROUP joining BY from1;
store grouped into 'homework6-5';
