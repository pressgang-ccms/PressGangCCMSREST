insert into CLIENT (CLIENT_ID, CLIENT_IDENTIFIER, CLIENT_NAME, CLIENT_REDIRECT_URI, GRANTS_MUST_EXPIRE) values (-1, 'publicclientid', 'PublicClient', 'https://localhost:8443/testauthserver', true)
insert into CLIENT (CLIENT_ID, CLIENT_IDENTIFIER, CLIENT_NAME, CLIENT_REDIRECT_URI, GRANTS_MUST_EXPIRE, CLIENT_SECRET) values (-2, 'confidentialclientid', 'ConfidentialClient', 'https://localhost:8443/testauthserver/confidential', true, 'clientsecret')

insert into OPENID_PROVIDER (PROVIDER_ID, PROVIDER_NAME, PROVIDER_URL) values (-1, 'Google', 'gmail.com')
insert into OPENID_PROVIDER (PROVIDER_ID, PROVIDER_NAME, PROVIDER_URL) values (-2, 'Yahoo', 'me.yahoo.com')

insert into OPENID_USER (USER_ID) values (-1)
insert into OPENID_USER (USER_ID) values (-2)

insert into OPENID_IDENTITY (IDENTITY_ID, IDENTIFIER, PROVIDER_ID, USER_ID) values (-1, 'https://www.google.com/accounts/o8/id?id=googleuser', -1, -1)
insert into OPENID_IDENTITY (IDENTITY_ID, IDENTIFIER, PROVIDER_ID, USER_ID) values (-2, 'https://me.yahoo.com/a/yahoouser', -2, -2)

update OPENID_USER set PRIMARY_IDENTITY_ID=-1 where USER_ID=-1
update OPENID_USER set PRIMARY_IDENTITY_ID=-2 where USER_ID=-2

insert into SCOPE (SCOPE_ID, SCOPE_NAME) values (-1, 'DEFAULT')
insert into SCOPE (SCOPE_ID, SCOPE_NAME) values (-2, 'PERFORM_USER_MANAGEMENT')

insert into OPENID_USER_SCOPE (USER_ID, SCOPE_ID) values (-1, -1)
insert into OPENID_USER_SCOPE (USER_ID, SCOPE_ID) values (-1, -2)
insert into OPENID_USER_SCOPE (USER_ID, SCOPE_ID) values (-2, -1)
insert into OPENID_USER_SCOPE (USER_ID, SCOPE_ID) values (-2, -2)

insert into TOKEN_GRANT (TOKEN_GRANT_ID, ACCESS_TOKEN, REFRESH_TOKEN, ACCESS_TOKEN_EXPIRES, ACCESS_TOKEN_EXPIRY, CLIENT_ID, USER_ID, TOKEN_GRANT_CURRENT, TOKEN_GRANT_TIMESTAMP) values (-1, 'access_token', 'refresh_token', true, '3600', -1, -1, true, CURRENT_TIMESTAMP())

insert into TOKEN_GRANT_SCOPE (TOKEN_GRANT_ID, SCOPE_ID) values (-1, -1)
insert into TOKEN_GRANT_SCOPE (TOKEN_GRANT_ID, SCOPE_ID) values (-1, -2)

insert into RS_SCOPE (SCOPE_ID, SCOPE_NAME) values (-1, 'DEFAULT')
insert into RS_SCOPE (SCOPE_ID, SCOPE_NAME) values (-2, 'PERFORM_USER_MANAGEMENT')

insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-1, 'https://localhost:8443/testauthserver/rest/auth/user/associate', 'POST', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-2, 'https://localhost:8443/testauthserver/rest/auth/confidential/user/associate', 'POST', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-3, 'https://localhost:8443/testauthserver/rest/auth/user/makeIdentityPrimary', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-4, 'https://localhost:8443/testauthserver/rest/auth/user/queryIdentity', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-5, 'https://localhost:8443/testauthserver/rest/auth/user/queryUser', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-6, 'https://localhost:8443/testauthserver/rest/auth/invalidate', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-7, 'https://localhost:8443/testauthserver/rest/auth/confidential/invalidate', 'GET', false)

insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -1)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -2)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -3)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -4)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -5)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -6)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -7)