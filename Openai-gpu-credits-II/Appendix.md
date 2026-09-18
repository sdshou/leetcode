The core behavior of the system, along with concrete walkthroughs of how overlapping intervals, usage events, and out-of-order data function in practice, can be illustrated by the following examples.

## 1. Standard Sequential Walkthrough

This example demonstrates normal chronological event arrivals. Note that subtraction events are often evaluated dynamically (e.g., pulling from the earliest expiring available credits) or tracked on a per-timestamp basis depending on the exact variation. [1, 2] 
Suppose you run these operations:

1. grantCredit("a", 10, 10, 40) → Credit "a" provides 10 units active from $t \in [10, 39]$.
2. grantCredit("b", 5, 20, 60) → Credit "b" provides 5 units active from $t \in [20, 59]$.
3. getBalance(15) → Returns 10 (Only "a" is active).
4. getBalance(25) → Returns 15 (Both "a" and "b" are active: 10 + 5).
5. subtract(3, 30) → Deducts 3 units at t = 30. If using an Earliest Deadline First (EDF) strategy to preserve credits, it consumes 3 units from "a" because "a" expires sooner (t=40) than "b" (t=60).
6. getBalance(35) → Returns 12 (7 left from "a" + 5 from "b").
7. getBalance(45) → Returns 5 ("a" has naturally expired; only "b"'s 5 units remain active). [1, 2, 3, 4]

---



## 2. Out-of-Order Event Handling

This example demonstrates what happens when network latency causes a consumption log to arrive before the underlying credit grant is registered by the system. [5, 6] 
Suppose events arrive at your system in this order:

1. subtract(4, 25) → The system records a deduction of 4 units at t=25. At this exact moment, if no grants have been recorded yet, a provisional state or negative event is logged internally. [2, 6, 7] 
2. getBalance(25) → Returns -1 or throws an error, because recorded consumption (4) exceeds registered available credits (0). [2, 7] 
3. grantCredit("c", 10, 10, 50) → Credit "c" provides 10 units spanning $t \in [10, 49]$. [4] 
4. getBalance(25) → Returns 6. Even though the grant was registered after the subtraction in real-world processing time, the system resolves the time-history correctly: at t=25, 10 units were valid, and 4 were used. [5, 6]

---



## 3. Edge Case: Point-in-Time Subtractions vs. Interval-Wide Deductions

Depending on the specific interviewer variation, subtraction logic behaves in one of two ways: [8] 


| Variation A: Point-in-Time (GPU Credits I)                                                                                                                                        | Variation B: Permanent Ledger (GPU Credits II)                                                                                                                                 |
| --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Subtractions apply only at that exact millisecond (like an API gateway rate spike check).                                                                                         | Subtractions permanently consume specific credit pools (like spending money from a wallet).                                                                                    |
| Example: 1. grantCredit("a", 5, 10, 50) 2. subtract(2, 20) 3. getBalance(20) → Returns 3 (5 - 2) 4. getBalance(25) → Returns 5 (The localized drop at t=20 does not impact t=25). | Example: 1. grantCredit("a", 5, 10, 50) 2. subtract(2, 20) 3. getBalance(20) → Returns 3 (5 - 2) 4. getBalance(25) → Returns 3 (2 credits are permanently gone from the pool). |


To write the most accurate implementation, which variation are you preparing for?

* 

- Point-in-Time: Subtractions only impact the balance at that exact timestamp.
- Permanent Ledger (EDF): Subtractions consume a specific grant pool based on expiration priority.
- 

I can provide a complete code block in your preferred programming language once we isolate the rules.

[1] [https://prachub.com](https://prachub.com/interview-experiences/openai-software-engineer-interview-experience-a-gpu-credit-coding-question-then-a-surprise-fullstack-design-round)
[2] [https://www.hacktherounds.com](https://www.hacktherounds.com/problem/3)
[3] [https://www.youtube.com](https://www.youtube.com/watch?v=4GoRvRNlE9E)
[4] [https://voker.io](https://voker.io/explore/interview/question/openai-coding-interview-time-based-gpu-credit-system)
[5] [https://www.hack2hire.com](https://www.hack2hire.com/question-bank/companies/openai/coding-questions/6913b4290ac116974ccedd12/practice)
[6] [https://www.hacktherounds.com](https://www.hacktherounds.com/problem/3)
[7] [https://www.reddit.com](https://www.reddit.com/r/Hack2Hire/comments/1p2ae9e/openai_screening_interview_gpu_credits/)
[8] [https://www.hack2hire.com](https://www.hack2hire.com/forum/6a49f2e13363604ec3fc821e)