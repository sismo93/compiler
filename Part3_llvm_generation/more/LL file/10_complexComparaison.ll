@.strR = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}
declare i32 @__isoc99_scanf(i8*, ...)
@.strP = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)
  ret void
}
declare i32 @printf(i8*, ...)
define void @ComplexComparaison(){
%a_0 = alloca i32
store i32 112, i32* %a_0
%b_0 = alloca i32
store i32 2, i32* %b_0
%1 = mul i32 54, 1000
%2 = mul i32 %1, 9696
%3 = sdiv i32 59654, 53
%4 = mul i32 %3, 585
%5 = add i32 %4, 56
%6 = add i32 %2, %5
%7 = sub i32 948, %6
%8 = load i32, i32* %a_0
%9 = icmp sgt i32 %8, %7
br i1 %9, label %ifCode9, label %elseCode9
br label %ifCode9
ifCode9:
store i32 1, i32* %a_0
br label %restCode9
br label %elseCode9
elseCode9:
br label %restCode9
restCode9:
%12 = load i32, i32* %b_0
call void @println(i32 %12)
ret void
}

define i32 @main(){
call void @ComplexComparaison()
ret i32 0
}
