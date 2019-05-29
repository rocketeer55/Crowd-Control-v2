uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

void main()
{
    v_Color = a_Color;

    vec4 new_Position = u_MVPMatrix * a_Position;
    new_Position.y = max(new_Position.y, -2.0);

    gl_Position = new_Position;
}