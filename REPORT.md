# Report: dynamic programming and string search

Nursultan Duisenbekuly, group SE-2524. DAA Assignment 4.

## 1. Experiment Design Card

This card, predictions included, was committed before the experiment was run for the first time (`git log` shows the order). Anything I change afterwards is listed in section 7 with the reason.

What holds for every row of both cards: random seed 42 (each case derives its own seed from it, so adding a case never changes the others), a JIT warm-up before timing, 5 timed runs per case, and the median goes into the plots. Counters come out identical on every run and on every machine. Times do not, so I never use a time as my only evidence. I picked the sizes from the formulas below, without a trial run.

### Edit distance

| Card field | What I wrote |
|---|---|
| Question | Does the work of the plain recursion blow up exponentially with the string length, while the table and two-row versions grow like n·m? And what do two rows save compared with the full table? |
| Sizes (n) | n = \|a\| = \|b\|.<br>Recursive: 2, 4, 6, 8, 10, 12. On the worst input every extra character multiplies the number of calls by about 5.8. At n = 12 that is already about 377 million calls, and n = 14 would need about 30 times more, so I stop at 12 (12 / 2 = 6, above the required factor of 4).<br>Table and two rows: the same six sizes, so all three versions share one axis, plus 250, 500, 1000, 2000 and 4000 (4000 / 250 = 16). At 4000 the table has 16 million cells (64 MB), which a default JVM heap holds easily. |
| Inputs | identical: a = b, the best case for the recursion because it only follows the diagonal.<br>random: two independent random strings over a-z, the typical case.<br>disjoint: a uses only a-m and b only n-z, so no two characters ever match. This is the worst case for the recursion.<br>Table and two rows should cost the same on all three. I want to see that, not assume it. |
| Metrics | time_ms: median of 5 runs. It is what a user feels, but it depends on the machine.<br>subproblems: recursive calls, or table cells filled. It counts the repeated work from Lecture 8 and is the same everywhere.<br>memory_cells: ints allocated for the table or the rows. It is what separates the table from two rows. |
| Prediction | 1. identical: the recursion makes exactly n + 1 calls.<br>2. disjoint: (3·D(n,n) − 1) / 2 calls, where D(n,n) is the central Delannoy number. That is 377,393,953 at n = 12, and about 5.8 times more for each extra character.<br>3. random: about half of the disjoint count at n = 12 (I expect between 25% and 90%).<br>4. table and two rows: exactly (n+1)^2 subproblems on every input. Memory is (n+1)^2 cells for the table and 2(n+1) for two rows.<br>5. time: the recursion takes between 0.5 s and 5 s per call at n = 12 on disjoint strings. Table and two rows get about 4 times slower when n doubles and need 20 to 150 ms at n = 4000. Two rows is as fast as the table or up to twice as fast. |
| Plots | edit_distance.png: subproblems against n on a log axis, the recursion on all three inputs plus the table. Does the recursion blow up while the table does not?<br>two_rows_vs_table.png: table ÷ two rows for memory and for time, against n (a ratio, log axis). What do two rows save, and does it cost time? |

### String search

| Card field | What I wrote |
|---|---|
| Question | Does Naive degrade to about n·m comparisons on a repetitive input while KMP stays within 2n? Does the difference show up on random text and on the clock? |
| Sizes (n) | n = text length, m = pattern length.<br>Sweep A: n = 125,000; 250,000; 500,000; 1,000,000; 2,000,000 with m = 64 (largest / smallest = 16).<br>Sweep B: n = 1,000,000 with m = 8, 16, 32, 64, 128, 256 (a factor of 32).<br>Log files are long and patterns are short, so the text length is the main size. Sweep B tests the n·m claim directly. The heaviest case, Naive on the worst input at m = 256, is about 256 million comparisons, a second or two. |
| Inputs | best_case: text without 'z' and a pattern starting with 'z', so Naive fails at the first comparison of every start.<br>random: random text over a-z with a random pattern planted five times, like a typical log search.<br>worst_case: text a^n, pattern a^(m-1)b. Naive matches m − 1 characters at every start and then fails. This is also the nastiest input for KMP.<br>all_matches: text a^n, pattern a^m. Every start is a match (n − m + 1 overlapping matches), so the output list is huge. |
| Metrics | time_ms: median of 5 runs. KMP's time includes building the LPS array.<br>comparisons: text-vs-pattern character comparisons in the search loop, the usual cost model of Lecture 9.<br>lps_comparisons: comparisons made while building the LPS array.<br>memory_cells: ints in the LPS array (0 for Naive). |
| Prediction | 1. Naive comparisons: best_case n − m + 1; random about 1.04 per start position; worst_case and all_matches exactly (n − m + 1)·m.<br>2. KMP comparisons: best_case n; random between n and 1.1n; all_matches n; worst_case 2n − m + 1, which is almost its 2n bound. lps_comparisons is at most 2m.<br>3. On random and best_case, the time of Naive divided by the time of KMP is between 0.5 and 2.<br>4. On worst_case at n = 1,000,000 and m = 64, Naive is between 5 and 30 times slower than KMP. That is less than the comparison ratio of about 32, because one KMP step costs more than one Naive comparison. From m = 8 to m = 256 the ratio grows by a factor between 10 and 40.<br>5. On all_matches the ratio is smaller than on worst_case, because both algorithms have to build a list of about n matches. |
| Plots | string_search.png: comparisons per text character against m (log-log), Naive and KMP on worst_case and random. Does Naive's work per character grow with m while KMP's stays flat?<br>performance_comparison.png: Naive time ÷ KMP time against n for m = 64, one line per input (a ratio, log axis). How much slower is Naive on the clock, and on which inputs? |
