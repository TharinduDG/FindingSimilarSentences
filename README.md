# Finding Similar Sentences

The task is to quickly find the number of pairs of sentences that are at the word-level edit distance at most 1. 
Two sentences S1 and S2 they are at edit distance 1 if S1 can be transformed to S2 by: adding, removing or substituting a single word.

For example, consider the following sentences where each letter represents a word:
• S1: A B C D
• S2: A B X D
• S3: A B C
• S4: A B X C

Then pairs the following pairs of sentences are at word edit distance 1 or less: (S1, S2), (S1, S3), (S2, S4), (S3, S4).
