# insight_challenge
In this challenge we processed H1B visa data using simple data structures to figure out which states and occupations have 
the most certified H1B visas.

We first parsed through the whole csv files in using a HashMaps as a counters. If a line corresponded to a certified visa we increment its counter in the map (or insert it with a value of 1), the key being state/ occupation. At the end we sort all the values in the map. Giving us a runtime in n log(n). 

Another approach was attempted that has a better asymptotic runtime O(n). In this approach, in addition to the hashmaps for counting we also kept a TreeMap (Red-Black Tree) of the top 10 states/ occupations. In this approach whenever we increase a counter we check if it is larger than the smallest in the TreeMap and if it is, we pop the smallest from the TreeMap and insert the new value (we also insert if the size of the tree is < 10). In this case there is no need to sort the result at the end. Also, since the tree is never larger than 10 we can guarantee asymptotically constant operations, leading to an overall linear time complexity. However, even for the larger csv files this did not seem to improve timing performance. The opposite actually it became a bit slower. I suspect the additional overhead of finding the smallest one in the tree and the pushes and pops were not worth the later savings. Plus, it made the code more comlex and difficult to read for other people so I decided to stick to approach 1.

