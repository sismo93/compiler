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
define void @While(){
%x_0 = alloca i32
store i32 10, i32* %x_0
br label %whileCond0
whileCond0:
%1 = load i32, i32* %x_0
%2 = mul i32 %1, 2
%3 = mul i32 1, 25
%4 = sub i32 2, %3
%5 = add i32 %2, %4
%6 = icmp slt i32 %5, 100
br i1 %6, label %whileCode0, label %whileRest0
br label %whileCode0
whileCode0:
%8 = load i32, i32* %x_0
%9 = mul i32 %8, 2
store i32 %9, i32* %x_0
%10 = load i32, i32* %x_0
call void @println(i32 %10)
br label %whileCond0
br label %whileRest0
whileRest0:
ret void
}

define i32 @main(){
call void @While()
ret i32 0
}
