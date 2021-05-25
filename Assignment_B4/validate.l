%{
#include<stdio.h>
#include "validate.tab.h"
%}
%%
int|float|char|double   return DT;
","     return COMMA;
";"     return SC;
[a-zA-Z][a-zA-Z0-9_]*   return ID;
[0-9]+ return NUM;
"="     return EQ;
%%

int yywrap()
{
    return 1;
}