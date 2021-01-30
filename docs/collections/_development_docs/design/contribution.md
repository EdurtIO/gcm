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

### Submitting a patch

1. It's generally best to start by opening a new issue describing the bug or
   feature you're intending to fix.  Even if you think it's relatively minor,
   it's helpful to know what people are working on.  Mention in the initial
   issue that you are planning to work on that bug or feature so that it can
   be assigned to you.

2. Follow the normal process of [forking](https://help.github.com/articles/fork-a-repo) the project, and setup a new
   branch to work in.  It's important that each group of changes be done in
   separate branches in order to ensure that a pull request only includes the
   commits related to that bug or feature.

3. Any significant changes should almost always be accompanied by tests.  The
   project already has good test coverage, so look at some of the existing
   tests if you're unsure how to go about it.

4. All contributions must be licensed Apache 2.0 and all files must have
   a copy of the boilerplate licence comment (can be copied from an existing
   file.  Files should be formatted according to [java style guide](https://raw.githubusercontent.com/EdurtIO/incubator-gcm/master/style.xml).

5. Do your best to have well-formed commit messages for each change.
   This provides consistency throughout the project, and ensures that commit
   messages are able to be formatted properly by various git tools.

6. Finally, push the commits to your fork and submit a [pull request](https://github.com/EdurtIO/incubator-gcm/pulls).

### Submitting documentation

The writing path of the document is in the `docs/collections` directory. Copy a file in any subdirectory to modify it.


> Warning: You need to install gpg tools, otherwise the code can not pass the verification. The gpg installation method can be obtained through online self-service query.
{: .explainer}
