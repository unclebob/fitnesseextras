#include <security/pam_appl.h>
#include <security/pam_misc.h>
#include <stdio.h>

char *password;

int custom_conv(int num_msg,const struct pam_message **msgm, struct pam_response **resp,void *appdata)
{
        struct pam_response *r;
       	r = (struct pam_response*)calloc(num_msg,sizeof(struct pam_response));
	if(num_msg > 0)
	{
        	r[0].resp_retcode = 0;                
        	r[0].resp = password;
	}       
        *resp = r;
        return PAM_SUCCESS;
}

static struct pam_conv conv = {
    custom_conv,
    NULL
};

void getPassword()
{
        int i=0;
	char c;
        char *buf=(char *)malloc(20*sizeof(char));
	buf[19] = '\0';
        while((c = getchar()) != '\n')
                        buf[i++] = c;
        buf[i] = '\0';
	password = buf;
}

int main(int argc, char *argv[])
{
	pam_handle_t *pamh=NULL;
	int retval;
	const char *user="nobody";

	if(argc == 2) 
		user = argv[1];

	if(argc != 2)
	{
		fprintf(stderr, "Usage: validate [username]\n");
		exit(1);
	}

	getPassword();
	
	retval = pam_start("fitnesse", user, &conv, &pamh);
        
	if (retval == PAM_SUCCESS)
		retval = pam_authenticate(pamh, 0);
	if (retval == PAM_SUCCESS)
		retval = pam_acct_mgmt(pamh, 0);
	
	if(retval == PAM_DISALLOW_NULL_AUTHTOK)
		printf("PAM_DISALLOW_NULL_AUTHTOK");
	else if(retval == PAM_AUTH_ERR)
		printf("PAM_AUTH_ERR");
	else if(retval == PAM_CRED_INSUFFICIENT)
		printf("PAM_CRED_INSUFFICIENT");
	else if(retval == PAM_AUTHINFO_UNAVAIL)
		printf("PAM_AUTHINFO_UNAVAIL");
	else if(retval == PAM_USER_UNKNOWN)
		printf("PAM_USER_UNKNOWN");
	else if(retval == PAM_MAXTRIES)
		printf("PAM_MAXTRIES");

	if (retval == PAM_SUCCESS)
	{
		printf("OK\n");
		return 0;
	}
	else
	{
		printf("DENIED\n");
		return 1;
	}
}

