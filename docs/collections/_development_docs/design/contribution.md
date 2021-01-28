---
title: How to Contribution?
category: Design
order: 2
---

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
