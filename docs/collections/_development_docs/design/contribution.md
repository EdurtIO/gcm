---
title: How to Contribution?
category: Design
order: 2
---

First of all, we need to clone the source code to the local, refer to the following shell script

```bash 
git clone https://github.com/EdurtIO/incubator-gcm.git
```

Clone a branch of your own for code contribution

```bash 
git checkout -b patch-xxxx
```

`xxxx`: GitHub username OR other

After modifying the source code, we need to test the source code

```bash 
./bin/code.sh check time1
```

`time1`: User defined string input at will

When all the code has passed the check, submit the code to its own branch

```bash 
git add .
git commit -m 'xxxx'
git push
```

Finally, click [here](https://github.com/EdurtIO/incubator-gcm/pulls) to submit the pull request for merging, and wait for the administrator to confirm the merging code. Generally, it takes 1-2 working days to complete the merging.

> Warning: You need to install gpg tools, otherwise the code can not pass the verification. The gpg installation method can be obtained through online self-service query.
{: .explainer}
