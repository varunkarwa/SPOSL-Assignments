%{
#include<stdio.h>
#include "string.h"
struct Symbol{
    char name[20];
}Sym[10];
void insert(char*);
int flag=0;
int i;
int cnt=0;
int commentflag=0;

%}

%%
"import"+" "+"java."[a-zA-Z]*+".*;"                                     {if(commentflag==0){printf("Preprocessor directives: %s",yytext);}else{printf(yytext);}printf("\n");}
"void"|"main"|"public"|"System"|"out"|"println"|"class"|"static"        {if(commentflag==0){printf("keywords: %s",yytext);}else{printf(yytext);}printf("\n");}
"public"+" "+"class"+" "+[_a-zA-Z]+[_]*[_a-zA-Z0-9]+                    {if(commentflag==0){size_t len=strlen(yytext);char subtext[10];strncpy(subtext,&yytext[13],len-13);subtext[len-13]='\0';printf("Identifier: %s", subtext);}else{printf(yytext);}printf("\n");}     
"int"|"String"|"char"                                                   {if(commentflag==0){printf("Data-Types: %s",yytext);}else{printf(yytext);}printf("\n");}
[)(}{]                                                                  {if(commentflag==0){if(commentflag==0){printf("Brackets: %s",yytext);}else{printf(yytext);}printf("\n");}}
"//"+[a-zA-Z0-9]*                                                       {if(commentflag==0){printf("Comment: %s",yytext);}else{printf(yytext);}printf("\n");}
"++"|"--"+[_a-zA-Z0-9]+"."+[_a-zA-Z0-9]+                                {if(commentflag==0){printf("Unary Operator: %s",yytext);}else{printf(yytext);}printf("\n");}
[-=+/*]                                                                 {if(commentflag==0){printf("Arithmetic Operators: %s",yytext);}else{printf(yytext);}printf("\n");}
[,.;]                                                                   {if(commentflag==0){printf("Delimiter: %s",yytext);}else{printf(yytext);}printf("\n");}
[0-9]+                                                                  {if(commentflag==0){printf("Number: %s",yytext);}else{printf(yytext);}printf("\n");}
[_a-zA-Z][_a-zA-Z0-9]*                                                  {if(commentflag==0){printf("Variables: %s",yytext);insert(yytext);}else{printf(yytext);}printf("\n");}
"/*"                                                                    {commentflag=1;printf("Comment Starts:");}
"*/"                                                                    {commentflag=0;printf("Comment Ends");printf("\n");}
%%
void insert(char* x)
{
    for(i=0;i<cnt;i++)
    {
        if(strcmp(x,Sym[i].name))
        {
            flag=1;
            break;
        }
    }

      if(flag==0)
      {
        strcpy(Sym[cnt].name,x);
        cnt++;
      }
}

void main()
{
FILE *fp;
fp = fopen("Main.java","r");
yyin = fp;
yylex();

printf("index | name");
printf("\n");
for(i=0;i<cnt;i++)
{
    printf("%d | ",i);
    printf("%s",Sym[i].name);
    printf("\n");
}
}

int yywrap()
{
return(1);
}