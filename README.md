# play-ovh plugin

This plugin adds [OVH](https://www.ovh.com/) API support to Play! Framework 1 applications.

# Features

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - ovh -> ovh 1.1.1

repositories:
    - sismicsNexusRaw:
        type: http
        artifact: "https://nexus.sismics.com/repository/sismics/[module]-[revision].zip"
        contains:
            - ovh -> *

```
####  Set configuration parameters

Add the following parameters to **application.conf**:

Use the prefix **ovh**, **soyoustart** or **kimsufi**.

```
# OVH configuration
# ~~~~~~~~~~~~~~~~~~~~
ovh.mock=false
ovh.url=https://eu.api.ovh.com
ovh.appKey=12345678
ovh.consumerKey=12345678
ovh.appSecret=12345678
```
####  Use the API

```
MeOvhService service = OvhClient.get(OvhClient.Product.OVH).getMeService();
service.getBill("FR12345678");
```

####  Mock the OVH server in dev

We recommand to mock OVH in development mode and test profile.

Use the following configuration parameter:

```
ovh.mock=true
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
