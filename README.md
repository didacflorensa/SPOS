# SPOS
Client-Server WebApp for solving numerical optimization models in a private cloud environment

### Authors
* Kevin Borell <kborrells@gmail.com>
* Jordi Mateo <jmateo@diei.udl.cat>

This web app was coded at the [University of Lleida] (http://www.eps.udl.cat) by the [GCD Research Group] (http://gcd.udl.cat).

### How to generate the WAR.

* Check if exists a public folder into the project folder.
  * If not exists, then create it:
  ```{r, engine='bash', count_lines}
  mkdir public
  ```
 * If exists, then clean it: 
 ```{r, engine='bash', count_lines} 
 rm -rf public/* 
 ```
* Make the grunt build:
```{r, engine='bash', count_lines} 
cd yo/ 
grunt build 
cp -r yo/dist public
```
* Generate the war file in the target folder, you will need to replace the XXXX with the proper password.
```{r, engine='bash', count_lines}
mvn package -Djasypt.encryptor.password='XXXXX'
```
