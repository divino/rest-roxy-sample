xquery version "1.0-ml";

module namespace yourNSAlias = "http://marklogic.com/rest-api/resource/template";

declare namespace roxy = "http://marklogic.com/roxy";
declare namespace rapi = "http://marklogic.com/rest-api";

(:
 : TEST: curl --anyauth --user <username>:<password> -X <http-method-upper-case> "http://<marklogic-hostname>:<application-rest-port>/v1/resources/<filename>?rs:<param-name>=<param-value>
 : NOTE: the rest parameters requires "rs:" prefix.
 :
 : All methods will return a json document which will return the content based on it's type.
 :)

(:
 : TEST: curl --anyauth --user admin:admin -X GET "http://localhost:28040/v1/resources/template
 :)
declare 
%roxy:params("")
function yourNSAlias:get(
  $context as map:map,
  $params  as map:map
) as document-node()*
{
  (: this will be the response body :)
  document { <hello>Get Method XML World!</hello> }
};

(:
 : TEST: curl --anyauth --user admin:admin -X PUT http://localhost:28040/v1/resources/template -d ""
 :)
declare 
%roxy:params("")
function yourNSAlias:put(
    $context as map:map,
    $params  as map:map,
    $input   as document-node()*
) as document-node()?
{
  (: this will be the response body :)
  document { <hello>Put Method XML World!</hello> }
};

(:
 : TEST: curl --anyauth --user admin:admin -X POST http://localhost:28040/v1/resources/template -d ""
 :)
declare 
%roxy:params("")
%rapi:transaction-mode("update")
function yourNSAlias:post(
    $context as map:map,
    $params  as map:map,
    $input   as document-node()*
) as document-node()*
{
  (: this will be the response body :)
  document {
    object-node {
      "hello": "Post Method JSON World!"
    }
  }
};

(:
 : TEST: curl --anyauth --user admin:admin -X DELETE http://localhost:28040/v1/resources/template
 :)
declare 
%roxy:params("")
function yourNSAlias:delete(
    $context as map:map,
    $params  as map:map
) as document-node()?
{
  (: this will be the response body :)
  document {
    object-node {
      "hello": "Delete Method JSON World!"
    }
  }
};
