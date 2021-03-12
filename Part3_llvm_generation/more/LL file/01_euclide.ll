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
define void @defaultFunctionName0(){
%1 = call i32 @readInt()
%a_0 = alloca i32
store i32 %1, i32* %a_0
%2 = call i32 @readInt()
%b_0 = alloca i32
store i32 %2, i32* %b_0
br label %whileCond2
whileCond2:
%3 = load i32, i32* %b_0
%4 = icmp ne i32 %3, 0
br i1 %4, label %whileCode2, label %whileRest2
br label %whileCode2
whileCode2:
%c_0 = alloca i32
%6 = load i32, i32* %b_0
store i32 %6, i32* %c_0
br label %whileCond6
whileCond6:
%7 = load i32, i32* %a_0
%8 = load i32, i32* %b_0
%9 = icmp sge i32 %7, %8
br i1 %9, label %whileCode6, label %whileRest6
br label %whileCode6
whileCode6:
%11 = load i32, i32* %a_0
%12 = load i32, i32* %b_0
%13 = sub i32 %11, %12
store i32 %13, i32* %a_0
br label %whileCond6
br label %whileRest6
whileRest6:
%15 = load i32, i32* %a_0
store i32 %15, i32* %b_0
%16 = load i32, i32* %c_0
store i32 %16, i32* %a_0
br label %whileCond2
br label %whileRest2
whileRest2:
%18 = load i32, i32* %a_0
call void @println(i32 %18)
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
