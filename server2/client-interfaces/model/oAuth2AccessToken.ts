/**
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { OAuth2RefreshToken } from './oAuth2RefreshToken';


export interface OAuth2AccessToken { 
    additionalInformation?: any;
    expiration?: Date;
    expired?: boolean;
    expiresIn?: number;
    refreshToken?: OAuth2RefreshToken;
    scope?: Array<string>;
    tokenType?: string;
    value?: string;
}
