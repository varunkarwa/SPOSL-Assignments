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

%}

%%
"import"+" "+"java."[a-zA-Z]*+".*;"         {printf("Preprocessor directives: %s",yytext);printf("\n");}
"void"|"main"|"public"|"System"|"out"|"println"|"class"|"static"        {printf("keywords: %s",yytext);printf("\n");}
"int"|"String"|"char"                        {printf("Data-Types: %s",yytext);printf("\n");}
[)(}{]                                                   {printf("Brackets: %s",yytext);printf("\n");}
[/]+[a-zA-Z0-9]*                                                 {printf("Comment: %s",yytext);printf("\n");}
[-=+/*]                                                    {printf("Arithmetic Operators: %s",yytext);printf("\n");}
[,.;]                                                   {printf("Delimiter: %s",yytext);printf("\n");}
[0-9]+                                     {printf("Number: %s",yytext);printf("\n");}
[_a-zA-Z][_a-zA-Z0-9]*                              {printf("Variables: %s",yytext);insert(yytext);printf("\n");}

%%
void insert(char* x)
{
    for(int i=0;i<cnt;i++)
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
for(int i;i<cnt;i++)
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